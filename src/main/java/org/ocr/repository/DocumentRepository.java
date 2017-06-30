package org.ocr.repository;

import org.ocr.domain.Document;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import java.util.List;

/**
 * Spring Data JPA repository for the Document entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DocumentRepository extends JpaRepository<Document,Long> {

    @Query("select document from Document document where document.user.login = ?#{principal.username}")
    List<Document> findByUserIsCurrentUser();
    
}
