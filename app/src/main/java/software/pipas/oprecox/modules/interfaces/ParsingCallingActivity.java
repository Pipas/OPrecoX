package software.pipas.oprecox.modules.interfaces;

import software.pipas.oprecox.modules.dataType.Ad;

public interface ParsingCallingActivity
{
    void addAdd(Ad a);
    void closeProgressPopup();
    void setShownAd(Ad a);
}
