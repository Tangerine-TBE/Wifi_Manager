package com.example.module_tool.flashlight_controller;

/**
 * Created by Luooh on 2017/8/7.
 */
public interface FlashlightListener {

    /**
     * Called when there is an error that turns the flashlight off.
     */
    void onFlashlightError(int errorCode);

    /**
     * Called when there is a change in availability of the flashlight functionality
     * @param available true if the flashlight is currently available.
     */
    void onFlashlightStateChanged(boolean available);
}
