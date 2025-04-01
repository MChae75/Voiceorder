package portfolio_2025.order_reiciver.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import portfolio_2025.order_reiciver.service.NLPService;
import portfolio_2025.order_reiciver.service.STTService;
import portfolio_2025.order_reiciver.service.TTSService;
import software.amazon.awssdk.services.polly.model.OutputFormat;
import software.amazon.awssdk.services.polly.model.VoiceId;

import java.io.InputStream;

@RestController
@RequestMapping("/voice")
public class VoiceController {

    private final STTService speechService;
    private final NLPService nlpService;
    private final TTSService textToSpeech;

    @Autowired
    public VoiceController(STTService speechService, NLPService nlpService) {
        this.speechService = speechService;
        this.nlpService = nlpService;
        this.textToSpeech = new TTSService(); // Alternatively, configure as a Spring bean
    }

    // Endpoint for speech recognition (speech-to-text)
    @PostMapping("/transcribe")
    public ResponseEntity<String> transcribeAudio(@RequestBody byte[] audioData) {
        // Convert byte[] to InputStream for processing
        InputStream audioStream = new java.io.ByteArrayInputStream(audioData);
        try {
            speechService.transcribeAudioStream(audioStream);
            return ResponseEntity.ok("Transcription in progress...");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("Error transcribing audio: " + e.getMessage());
        }
    }

    // Endpoint for text-to-speech (using Amazon Polly)
    @GetMapping("/synthesize")
    public ResponseEntity<String> synthesizeSpeech(@RequestParam String text) {
        try {
            InputStream audioStream = textToSpeech.synthesizeSpeech(text, OutputFormat.MP3, VoiceId.JOANNA);
            // In a real application, you might return the audio stream as a file download or stream it directly.
            return ResponseEntity.ok("Speech synthesized successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("Error synthesizing speech: " + e.getMessage());
        }
    }

    // Optional endpoint to parse order text using NLP
    @PostMapping("/parse")
    public ResponseEntity<String> parseOrder(@RequestBody String orderText) {
        String parsedResponse = nlpService.parseOrderText(orderText);
        return ResponseEntity.ok(parsedResponse);
    }
}