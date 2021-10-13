package bio.ferlab.config;

import bio.ferlab.pac4j.config.AwsTools;
import bio.ferlab.pac4j.config.OidcEncryptedConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class OidcEncryptedConfigurationTest {
    private static final String SECRET = "SECRET";
    private static final String DECRYPTED = "DECRYPTED";
    private static final String KMS_KEY_ID = "kms-key-id";
    private static final String REGION = "us-east-1";

    @Mock
    AwsTools awsTools;

    OidcEncryptedConfiguration oidcEncryptedConfiguration;

    @BeforeEach
    void setup() {
        oidcEncryptedConfiguration = new OidcEncryptedConfiguration();
        oidcEncryptedConfiguration.setAwsRegion(REGION);
        oidcEncryptedConfiguration.setAwsTools(awsTools);
    }

    @Test
    void whenGetSecretIsNotEncryptedThenDontDecrypt() {
        oidcEncryptedConfiguration.setSecretEncrypted(false);
        oidcEncryptedConfiguration.setSecret(SECRET);

        assertEquals(SECRET, oidcEncryptedConfiguration.getSecret());
    }
    @Test
    void whenGetSecretIsEncryptedThenDecrypt() {
        oidcEncryptedConfiguration.setSecretEncrypted(true);
        oidcEncryptedConfiguration.setSecret(SECRET);
        oidcEncryptedConfiguration.setKmsKeyId(KMS_KEY_ID);
        Mockito.when(awsTools.kmsDecrypt(SECRET, REGION, KMS_KEY_ID)).thenReturn(DECRYPTED);

        assertEquals(DECRYPTED, oidcEncryptedConfiguration.getSecret());

        Mockito.verify(awsTools, Mockito.times(1)).kmsDecrypt(SECRET, REGION, KMS_KEY_ID);
    }
}
