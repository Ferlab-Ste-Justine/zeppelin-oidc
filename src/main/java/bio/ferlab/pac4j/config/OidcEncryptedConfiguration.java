package bio.ferlab.pac4j.config;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.pac4j.oidc.config.OidcConfiguration;

@Slf4j
public class OidcEncryptedConfiguration extends OidcConfiguration {

    public static final String SECRET_ENCRYPTED = "secretEncrypted";

    public static final String KMS_KEY_ID = "kmsKeyId";

    public static final String AWS_REGION = "awsRegion";

    @Getter
    @Setter
    private AwsTools awsTools;

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
        log.debug("secretEncrypted: {}", this.isSecretEncrypted());

        if(this.isSecretEncrypted()) {
            if(awsTools == null) awsTools = new AwsTools();
            return awsTools.kmsDecrypt(super.getSecret(), this.getAwsRegion(), this.getKmsKeyId());
        }

        return super.getSecret();
    }
}
