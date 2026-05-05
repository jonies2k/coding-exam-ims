package org.demo.imsproject.dto;

public record CallForwardNoReplyDTO(
        boolean provisioned,
        String destination
) {}
