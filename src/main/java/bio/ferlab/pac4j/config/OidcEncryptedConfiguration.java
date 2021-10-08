package bio.ferlab.pac4j.config;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.pac4j.oidc.config.OidcConfiguration;
import software.amazon.awssdk.services.kms.KmsClient;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.services.kms.model.DecryptRequest;
import software.amazon.awssdk.services.kms.model.DecryptResponse;
import software.amazon.awssdk.services.kms.model.KmsException;
import software.amazon.awssdk.regions.Region;
import java.nio.charset.StandardCharsets;

@Slf4j
public class OidcEncryptedConfiguration extends OidcConfiguration {

    public static final String SECRET_ENCRYPTED = "secretEncrypted";

    public static final String KMS_KEY_ID = "kmsKeyId";

    public static final String AWS_REGION = "awsRegion";

    @Getter
    @Setter
    private boolean secretEncrypted;

    //keyId - the id of the AWS KMS key to use to encrpt/decrypt the data. You can obtain the key ID value from the AWS Management Console.
    @Getter
    @Setter
    private String kmsKeyId;

    @Getter
    @Setter
    private String awsRegion;

    @Override
    public String getSecret() {
        if(this.isSecretEncrypted()) {
            return kmsDecrypt(super.getSecret());
        } else {
            return super.getSecret();
        }
    }

    private String kmsDecrypt(String data) {
        try {
            KmsClient kmsClient = getKMSClient(this.getAwsRegion());
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

    // Return a KmsClient object
    private static KmsClient getKMSClient(String awsRegion) {
        Region region = Region.of(awsRegion);
        return KmsClient.builder()
                .region(region)
                .build();
    }
}
