package com.ssafy.trip.util;

import com.ssafy.trip.model.dao.MemberSecDao;
import com.ssafy.trip.model.vo.MemberSec;
import com.ssafy.trip.util.OpenCrypt;
import com.ssafy.trip.util.exception.common.PasswordEncodeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

@Slf4j
@Component
public class MyPasswordEncoder implements PasswordEncoder {

    private final MemberSecDao memberSecDao;
    private final String BASE_SALT;

    @Autowired
    public MyPasswordEncoder(MemberSecDao memberSecDao) {
        this.memberSecDao = memberSecDao;
        BASE_SALT = UUID.randomUUID().toString();
    }

    @Override
    public String encode(CharSequence rawPassword) {
        try {
            return encode(rawPassword, BASE_SALT);
        } catch(PasswordEncodeException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }
    public String encode(CharSequence rawPassword, String salt) throws PasswordEncodeException {
        try {
            if (rawPassword == null) {
                log.error("MyPasswordEncoder: 비밀번호 입력 실패");
                throw new IllegalArgumentException("비밀번호가 입력되지 않았습니다.");
            }
            if (salt == null) {
                log.error("MyPasswordEncoder: 암호키 입력 실패");
                throw new IllegalArgumentException("암호 키가 입력되지 않았습니다.");
            }

            return OpenCrypt.getSHA256(rawPassword.toString(), salt);
        } catch(Exception e) {
            log.error(e.getMessage());
            throw new PasswordEncodeException();
        }
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        try {
            return matches(rawPassword, encodedPassword, BASE_SALT);
        } catch (PasswordEncodeException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    public boolean matches(CharSequence rawPassword, String encodedPassword, String salt) throws PasswordEncodeException {
        try {
        // 해쉬 값 비교
        String hashPassword = OpenCrypt.getSHA256(rawPassword.toString(), salt);
        return encodedPassword.equals(hashPassword);
        } catch(Exception e) {
            log.error(e.getMessage());
            throw new PasswordEncodeException();
        }
    }

    public String getRandomSalt() {
        return UUID.randomUUID().toString();
    }
    public String getSalt(String memberId) {
        MemberSec memberSec = memberSecDao.findByMemberId(memberId);
        return memberSec.getSalt();
    }

    public byte[] generateKey() throws PasswordEncodeException {
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
            keyGenerator.init(128);
            SecretKey key = keyGenerator.generateKey();
            return key.getEncoded();
        } catch (NoSuchAlgorithmException e) {
            log.error(e.getMessage());
            throw new PasswordEncodeException();
        }
    }
}
