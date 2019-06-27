package com.schmidmt.demo.models.dao;

import com.schmidmt.demo.models.UploadedFile;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.beans.Transient;
import java.util.UUID;

@Repository
@Transactional
public interface UploadedFileRepository extends CrudRepository<UploadedFile, UUID> {
}
