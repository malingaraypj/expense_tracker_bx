package org.example.expense_tracker_bx.service;

import org.example.expense_tracker_bx.dto.response.DashboardSummaryDTO;
import org.springframework.stereotype.Service;

@Service
public interface DashboardService {
    public DashboardSummaryDTO getDashboardSummary(Long userId);
}
