package portfolio_2025.order_reiciver.service;

import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.async.SdkPublisher;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.transcribestreaming.TranscribeStreamingAsyncClient;
import software.amazon.awssdk.services.transcribestreaming.model.*;
import software.amazon.awssdk.core.SdkBytes;

import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Flow;

@Service
public class STTService {

    private final TranscribeStreamingAsyncClient transcribeClient;

    public STTService() {
        // Build the Transcribe client for a specific region
        // Credentials are picked up from the default AWS credential provider chain
        this.transcribeClient = TranscribeStreamingAsyncClient.builder()
                .region(Region.US_EAST_1)
                .build();
    }

    /**
     * Transcribe an InputStream of PCM audio (16kHz, 16-bit, mono).
     * This uses Amazon Transcribe Streaming to get partial/final transcripts in near real-time.
     */
    public void transcribeAudioStream(InputStream audioStream) throws Exception {
        // 1) Create a request describing language, sample rate, etc.
        StartStreamTranscriptionRequest request = StartStreamTranscriptionRequest.builder()
                .languageCode(LanguageCode.EN_US.toString()) 
                .mediaEncoding(MediaEncoding.PCM)
                .mediaSampleRateHertz(16000)
                .build();

        // 2) Start streaming. We provide:
        //    - The request object
        //    - A publisher for audio data
        //    - A response handler to process transcripts
        CompletableFuture<Void> resultFuture = transcribeClient.startStreamTranscription(
                request,
                new AudioStreamPublisher(audioStream),
                new TranscriptionResponseHandler()
        );

        // 3) Wait for the streaming session to complete or handle asynchronously
        resultFuture.get();
    }

    /**
     * Sends audio data from an InputStream in chunks to Amazon Transcribe.
     * The audio must be raw PCM with the correct sample rate and channels.
     */
    private static class AudioStreamPublisher implements software.amazon.awssdk.core.async.SdkPublisher<AudioStream> {
        private static final int CHUNK_SIZE = 1024; // Adjust as needed
        private final InputStream audioStream;

        public AudioStreamPublisher(InputStream audioStream) {
            this.audioStream = audioStream;
        }

        @Override
        public void subscribe(org.reactivestreams.Subscriber<? super AudioStream> subscriber) {
            subscriber.onSubscribe(new org.reactivestreams.Subscription() {
                @Override
                public void request(long n) {
                    try {
                        byte[] buffer = new byte[CHUNK_SIZE];
                        int bytesRead;
                        while ((bytesRead = audioStream.read(buffer)) != -1) {
                            // Wrap the audio bytes into a ByteBuffer
                            ByteBuffer audioBuffer = ByteBuffer.wrap(buffer, 0, bytesRead);
                            AudioEvent audioEvent = AudioEvent.builder()
                                    .audioChunk(SdkBytes.fromByteBuffer(audioBuffer))
                                    .build();
                            subscriber.onNext(audioEvent);
                        }
                        subscriber.onComplete();
                    } catch (Exception e) {
                        subscriber.onError(e);
                    }
                }

                @Override
                public void cancel() {
                    // Handle cancellation if needed
                }
            });
        }
    }

    /**
     * Processes real-time transcripts from Amazon Transcribe (partial/final).
     */
    private static class TranscriptionResponseHandler implements software.amazon.awssdk.services.transcribestreaming.model.StartStreamTranscriptionResponseHandler {
        
        @Override
        public void onEventStream(SdkPublisher<TranscriptResultStream> publisher) {
            publisher.subscribe(event -> {
                if (event instanceof TranscriptEvent) {
                    TranscriptEvent transcriptEvent = (TranscriptEvent) event;
                    transcriptEvent.transcript().results().forEach(result -> {
                        result.alternatives().forEach(alt -> {
                            String transcript = alt.transcript();
                            boolean isPartial = !Boolean.FALSE.equals(result.isPartial());
                            System.out.println((isPartial ? "[Partial]" : "[Final]") + " " + transcript);
                        });
                    });
                }
            });
        }

        @Override
        public void responseReceived(StartStreamTranscriptionResponse response) {
            System.out.println("Streaming session started. Request ID: " + response.requestId());
        }

        @Override
        public void exceptionOccurred(Throwable throwable) {
            System.err.println("Error during streaming: " + throwable.getMessage());
            throwable.printStackTrace();
        }

        @Override
        public void complete() {
            System.out.println("Streaming session completed.");
        }
    }
}