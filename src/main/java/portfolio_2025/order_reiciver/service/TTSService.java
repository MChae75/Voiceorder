package portfolio_2025.order_reiciver.service;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.polly.PollyClient;
import software.amazon.awssdk.services.polly.model.OutputFormat;
import software.amazon.awssdk.services.polly.model.SynthesizeSpeechRequest;
import software.amazon.awssdk.services.polly.model.SynthesizeSpeechResponse;
import software.amazon.awssdk.services.polly.model.VoiceId;

import java.io.InputStream;
public class TTSService {
        private final PollyClient polly;

    public TTSService() {
        // Initialize the Polly client; AWS credentials and region will be picked up automatically
        this.polly = PollyClient.builder()
                .region(Region.US_EAST_1)
                .build();
    }

    /**
     * Synthesize speech from the provided text using the specified voice.
     *
     * @param text    The text to synthesize.
     * @param format  The output format (e.g., MP3).
     * @param voiceId The voice to use (e.g., Joanna).
     * @return An InputStream containing the audio data.
     */
    public InputStream synthesizeSpeech(String text, OutputFormat format, VoiceId voiceId) {
        SynthesizeSpeechRequest request = SynthesizeSpeechRequest.builder()
                .text(text)
                .outputFormat(format)
                .voiceId(voiceId)
                .build();

        InputStream audioStream = polly.synthesizeSpeech(request);
        return audioStream;
    }
    
}