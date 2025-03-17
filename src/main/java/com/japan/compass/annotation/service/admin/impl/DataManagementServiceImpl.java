package com.japan.compass.annotation.service.admin.impl;

import com.japan.compass.annotation.domain.RecordFileList;
import com.japan.compass.annotation.domain.entity.Application;
import com.japan.compass.annotation.infrastructure.RecordFileComponent;
import com.japan.compass.annotation.repository.ApplicationRepository;
import com.japan.compass.annotation.service.admin.DataManagementService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@AllArgsConstructor
@Service
public class DataManagementServiceImpl implements DataManagementService {

    private final ApplicationRepository applicationRepository;
    private final RecordFileComponent recordFileComponent;


    @Override
    public RecordFileList getRecordFileList() {
        Application application = applicationRepository.find();
        List<String> list = recordFileComponent.listZip();
        return new RecordFileList(list, application == null ? null: application.getRecordLastUpdated());
    }

    @Override
    public Resource loadZip(String filename) {
        return recordFileComponent.loadZip(filename);
    }
}
