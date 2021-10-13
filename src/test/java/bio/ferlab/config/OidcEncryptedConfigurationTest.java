package bio.ferlab.config;

import bio.ferlab.pac4j.config.AwsTools;
import bio.ferlab.pac4j.config.OidcEncryptedConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import software.amazon.awssdk.services.kms.KmsClient;
import software.amazon.awssdk.services.kms.model.DecryptRequest;
import software.amazon.awssdk.services.kms.model.DecryptResponse;

import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class OidcEncryptedConfigurationTest {
    private static final String SECRET = "SECRET";

    @Mock
    AwsTools awsTools;

    OidcEncryptedConfiguration oidcEncryptedConfiguration;

    @BeforeEach
    void setup() {
        oidcEncryptedConfiguration = new OidcEncryptedConfiguration();
        oidcEncryptedConfiguration.setAwsRegion("us-east-1");
        oidcEncryptedConfiguration.setAwsTools(awsTools);
    }

    @Test
    void testGetSecret() {
        oidcEncryptedConfiguration.setSecretEncrypted(false);
        oidcEncryptedConfiguration.setSecret(SECRET);
        Mockito.when(awsTools.kmsDecrypt(any(), any(), any())).thenReturn(SECRET);

        assertEquals(SECRET, oidcEncryptedConfiguration.getSecret());

        oidcEncryptedConfiguration.setSecretEncrypted(true);
        Mockito.when(awsTools.kmsDecrypt(any(), any(), any())).thenReturn(SECRET);
        assertEquals(SECRET, oidcEncryptedConfiguration.getSecret());
    }
}
