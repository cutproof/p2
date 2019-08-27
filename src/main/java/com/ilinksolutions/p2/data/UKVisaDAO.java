package com.ilinksolutions.p2.data;

import java.util.List;
import com.ilinksolutions.p2.domains.UKVisaMessage;

/**
 *
 */
public interface UKVisaDAO
{
    void save(UKVisaMessage entry);
    List<UKVisaMessage> list();
}
