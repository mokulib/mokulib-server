package pers.liaohaolong.mokulibserver.service.business;

import pers.liaohaolong.mokulibserver.dto.response.DashboardDTO;

public interface DashboardService {

    void refresh();

    DashboardDTO get();

}
