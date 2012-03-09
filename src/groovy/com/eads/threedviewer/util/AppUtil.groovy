package com.eads.threedviewer.util

class AppUtil {

    static String generateRandomHex() {
        Random random = new Random()
        Integer number = random.nextInt(AppConstants.MIN_HEX) + AppConstants.MIN_HEX
        return Integer.toHexString(number)
    }
}
