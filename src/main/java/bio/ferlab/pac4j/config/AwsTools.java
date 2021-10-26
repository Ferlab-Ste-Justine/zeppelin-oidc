package bio.ferlab.pac4j.config;

import com.amazonaws.services.kms.AWSKMS;
import com.amazonaws.services.kms.AWSKMSClientBuilder;
import com.amazonaws.services.kms.model.DecryptRequest;
import com.amazonaws.services.kms.model.DecryptResult;
import com.amazonaws.util.Base64;
import lombok.extern.slf4j.Slf4j;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

@Slf4j
public class AwsTools {

    public String kmsDecrypt(String data, String awsRegion, String kmsKeyId) {
        AWSKMS kmsClient = AWSKMSClientBuilder.standard().withRegion(awsRegion).build();

        byte[] dataBytes = Base64.decode(data);

        ByteBuffer cipherBuffer = ByteBuffer.wrap(dataBytes);
        DecryptRequest req = new DecryptRequest().withCiphertextBlob(cipherBuffer).withKeyId(kmsKeyId);
        DecryptResult resp = kmsClient.decrypt(req);
        return new String(resp.getPlaintext().array(), StandardCharsets.UTF_8);
    }
}
