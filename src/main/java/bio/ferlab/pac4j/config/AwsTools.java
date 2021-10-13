package bio.ferlab.pac4j.config;

import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.kms.KmsClient;
import software.amazon.awssdk.services.kms.model.DecryptRequest;
import software.amazon.awssdk.services.kms.model.DecryptResponse;
import software.amazon.awssdk.services.kms.model.KmsException;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class AwsTools {
    private static Map<String, KmsClient> kmsClientMap = new HashMap<>();

    public KmsClient getKmsClient(String awsRegion) {
        KmsClient kmsClient = kmsClientMap.get(awsRegion);
        if(kmsClient == null) {
            Region region = Region.of(awsRegion);
            kmsClient = KmsClient.builder()
                    .region(region)
                    .build();
            kmsClientMap.put(awsRegion, kmsClient);
        }
        return kmsClient;
    }

    public String kmsDecrypt(String data, String awsRegion, String kmsKeyId) {
        try {
            KmsClient kmsClient = getKmsClient(awsRegion);
            SdkBytes encryptedData = SdkBytes.fromByteArray(data.getBytes(StandardCharsets.UTF_8));

            DecryptRequest decryptRequest = DecryptRequest.builder()
                    .ciphertextBlob(encryptedData)
                    .keyId(kmsKeyId)
                    .build();

            DecryptResponse decryptResponse = kmsClient.decrypt(decryptRequest);
            SdkBytes plainText = decryptResponse.plaintext();
            return new String(plainText.asByteArray(), StandardCharsets.UTF_8);

        } catch (KmsException e) {
            log.error(e.getMessage());
            System.exit(1);
        }
        return null;
    }
}
