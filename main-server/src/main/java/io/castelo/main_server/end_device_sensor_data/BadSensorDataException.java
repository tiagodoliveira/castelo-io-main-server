package io.castelo.main_server.end_device_sensor_data;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadSensorDataException extends RuntimeException{
}
