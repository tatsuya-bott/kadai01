package com.japan.compass.annotation.service.admin;

import com.japan.compass.annotation.domain.RecordFileList;
import org.springframework.core.io.Resource;

public interface DataManagementService {

    RecordFileList getRecordFileList();
    Resource loadZip(String filename);
}
