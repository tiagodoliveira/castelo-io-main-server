package io.castelo.main_server.end_device;

import io.castelo.main_server.data_validators.IpAddressValidator;
import io.castelo.main_server.data_validators.MACAddressValidator;
import io.castelo.main_server.end_device_component.EndDeviceComponent;
import io.castelo.main_server.end_device_component.EndDeviceComponentService;
import io.castelo.main_server.exception.ForbiddenException;
import io.castelo.main_server.exception.ResourceNotFoundException;
import io.castelo.main_server.gateway.Gateway;
import io.castelo.main_server.gateway.GatewayService;
import io.castelo.main_server.user.User;
import io.castelo.main_server.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class EndDeviceService {

    private final EndDeviceRepository endDeviceRepository;
    private final EndDeviceComponentService endDeviceComponentService;
    private final GatewayService gatewayService;
    private final UserService userService;

    @Autowired
    public EndDeviceService (EndDeviceRepository endDeviceRepository,
                             EndDeviceComponentService endDeviceComponentService,
                             GatewayService gatewayService, UserService userService) {
        this.endDeviceRepository = endDeviceRepository;
        this.endDeviceComponentService = endDeviceComponentService;
        this.gatewayService = gatewayService;
        this.userService = userService;
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @Transactional(readOnly = true)
    public List<EndDevice> getAllEndDevices() {
        return endDeviceRepository.findAll();
    }

    @Transactional(readOnly = true)
    public EndDevice getEndDevice(String endDeviceMac) {
        return verifyIfAuthenticatedUserHasAccessToEndDevice(endDeviceMac);
    }

    @Transactional
    public EndDevice createEndDevice(EndDevice endDevice) {
        validateEndDevice(endDevice);

        verifyIfAuthenticatedUserOwnsEndDevice(endDevice.getOwner().getUserId());

        if (endDevice.getWorkingMode() == null) {
            endDevice.setWorkingMode(WorkingModes.AUTONOMOUS);
        }

        endDevice = endDeviceRepository.save(endDevice);

        List<EndDeviceComponent> endDeviceComponents = endDeviceComponentService.createComponents(endDevice);

        endDevice.getComponents().clear();
        endDevice.getComponents().addAll(endDeviceComponents);

        return endDevice;
    }

    @Transactional
    public EndDevice updateEndDevice(EndDevice updatedEndDevice) {
        validateEndDevice(updatedEndDevice);
        verifyIfAuthenticatedUserHasAccessToEndDevice(updatedEndDevice.getEndDeviceMac());
        return endDeviceRepository.save(updatedEndDevice);
    }

    @Transactional
    public void deleteEndDevice(String endDeviceMac) {
        EndDevice endDevice = verifyIfAuthenticatedUserOwnsEndDevice(endDeviceMac);
        endDeviceRepository.delete(endDevice);
    }

    @Transactional
    public EndDevice pairWithGateway(String endDeviceMac, String gatewayMac) {
        EndDevice endDevice = verifyIfAuthenticatedUserHasAccessToEndDevice(endDeviceMac);
        Gateway gateway = verifyIfAuthenticatedUserHasAccessToGateway(gatewayMac);

        endDevice.setGateway(gateway);
        return endDeviceRepository.save(endDevice);
    }

    private void validateEndDevice(EndDevice endDevice) {
        IpAddressValidator.validateIpAddress(endDevice.getEndDeviceIp());
        endDevice.setEndDeviceMac(MACAddressValidator.normalizeMACAddress(endDevice.getEndDeviceMac()));

        if(endDevice.getGateway() != null)
            gatewayService.verifyIfGatewayExists(endDevice.getGateway().getGatewayMac());
    }

    private void verifyIfAuthenticatedUserOwnsEndDevice(UUID userId) {
        if (!userId.equals(getAuthenticatedUserId())) {
            throw new ForbiddenException("You don't have access to this device!");
        }
    }

    private EndDevice verifyIfAuthenticatedUserOwnsEndDevice(String existingDeviceMac) {
        EndDevice existingDevice = endDeviceRepository.findById(existingDeviceMac)
                .orElseThrow(() -> new ResourceNotFoundException("EndDevice not found with mac: " + existingDeviceMac));
        User authenticatedUser = userService.getUser(getAuthenticatedUserId());
        if (!existingDevice.getOwner().equals(authenticatedUser)) {
            throw new ForbiddenException("You don't have access to this device!");
        }
        return existingDevice;
    }

    private EndDevice verifyIfAuthenticatedUserHasAccessToEndDevice(String existingDeviceMac) {
        EndDevice existingDevice = endDeviceRepository.findById(existingDeviceMac)
                .orElseThrow(() -> new ResourceNotFoundException("EndDevice not found with mac: " + existingDeviceMac));
        User authenticatedUser = userService.getUser(getAuthenticatedUserId());
        if (!existingDevice.hasAccess(authenticatedUser)) {
            throw new ForbiddenException("You do not have permission to modify this device!");
        }
        return existingDevice;
    }

    private Gateway verifyIfAuthenticatedUserHasAccessToGateway(String gatewayMac) {
        Gateway gateway = gatewayService.getGateway(gatewayMac);
        User authenticatedUser = userService.getUser(getAuthenticatedUserId());

        if(gateway.hasAccess(authenticatedUser))
            return gateway;
        else
            throw new ForbiddenException("You don't have access to this gateway!");
    }

    private UUID getAuthenticatedUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User userDetails = (User) authentication.getPrincipal();
        return userDetails.getUserId();
    }
}
