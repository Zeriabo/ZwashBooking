package com.zwash.booking.serviceImpl;

import com.zwash.booking.service.WashService;
import com.zwash.common.pojos.Wash;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;

@Service
public class WashServiceImpl implements WashService {

    @Override
    public Wash getWash(Long id) throws NoSuchElementException {
        // TODO: fetch and return Wash by ID
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public Wash getWashByBooking(Long bookingId) throws NoSuchElementException {
        // TODO: fetch and return Wash by Booking ID
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public boolean startWash(Wash wash) {
        // TODO: implement logic
        return false;
    }

    @Override
    public boolean finishWash(Wash wash) {
        // TODO: implement logic
        return false;
    }

    @Override
    public boolean cancelWash(Wash wash) {
        // TODO: implement logic
        return false;
    }

    @Override
    public boolean rescheduleWash(Wash wash, LocalDateTime startTime) {
        // TODO: implement logic
        return false;
    }

    @Override
    public boolean buyWash(Wash wash) {
        // TODO: implement logic
        return false;
    }
}
