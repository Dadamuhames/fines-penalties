package com.uzumtech.finespenalties.service.intr.otp;

import com.uzumtech.finespenalties.dto.request.OtpByPinflRequest;
import com.uzumtech.finespenalties.dto.response.OtpResponse;

public interface OtpRequestService {

    OtpResponse sendByPinfl(OtpByPinflRequest request);
}
