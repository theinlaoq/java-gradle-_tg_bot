package ru.yoruuq.valuesbot.service;

import ru.yoruuq.valuesbot.exception.ServiceException;

public interface ValuesRateService {
    String getUSDRate() throws ServiceException;

    String getEURORate() throws ServiceException;
}
