package io.castelo.main_server.end_device_switch;

import io.castelo.main_server.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SwitchService {

    @Autowired
    private SwitchRepository switchRepository;

    public List<Switch> getAllSwitches() {
        return switchRepository.findAll();
    }

    public Switch getSwitch(SwitchKey id) {
        return switchRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Switch not found with id: " + id));
    }

    public Switch createSwitch(Switch sw) {
        return switchRepository.save(sw);
    }

    public Switch updateSwitchName(SwitchKey id, String switchName) {
        Switch sw = switchRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Switch not found with id: " + id));
        sw.setSwitchName(switchName);
        return switchRepository.save(sw);
    }

    public void deleteSwitch(SwitchKey id) {
        Switch sw = switchRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Switch not found with id: " + id));
        switchRepository.delete(sw);
    }
}
