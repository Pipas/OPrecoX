package software.pipas.oprecox.modules.interfaces;

import software.pipas.oprecox.modules.dataType.Add;

public interface ParsingCallingActivity
{
    void addAdd(Add a);
    void closeProgressPopup();
    void setShownAdd(Add a);
}
