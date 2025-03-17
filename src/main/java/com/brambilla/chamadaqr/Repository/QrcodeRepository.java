package com.brambilla.chamadaqr.Repository;

import com.brambilla.chamadaqr.Entity.Qrcode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface QrcodeRepository extends JpaRepository<Qrcode, Long> {
    List<Qrcode> findByCreatedAt(String createdAt);
    Optional<Qrcode> findByHash(String hash);

}