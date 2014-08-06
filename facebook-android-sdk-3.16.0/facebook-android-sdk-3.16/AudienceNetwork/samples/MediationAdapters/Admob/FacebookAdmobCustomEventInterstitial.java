/**
 * Copyright 2014 Facebook, Inc.
 *
 * You are hereby granted a non-exclusive, worldwide, royalty-free license to
 * use, copy, modify, and distribute this software in source code or binary
 * form for use in connection with the web and mobile services and APIs
 * provided by Facebook.
 *
 * As with any software that integrates with the Facebook platform, your use
 * of this software is subject to the Facebook Developer Principles and
 * Policies [http://developers.facebook.com/policy/]. This copyright notice
 * shall be included in all copies or substantial portions of the software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL
 * THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 * DEALINGS IN THE SOFTWARE.
 *
 */

package com.facebook.ads;

import android.app.Activity;
import com.google.ads.mediation.MediationAdRequest;
import com.google.ads.mediation.customevent.CustomEventInterstitial;
import com.google.ads.mediation.customevent.CustomEventInterstitialListener;

/**
 * FacebookAdmobCustomEventInterstitial is an admob adapter for InterstitialAd.
 * <p/>
 * Compatible with Google Mobile Ads SDK version 6.4.1
 * <p/>
 * Declare com.facebook.ads.InterstitialAdActivity in AndroidManifest.xml
 * with android:configChanges="keyboardHidden|orientation"
 */
public class FacebookAdmobCustomEventInterstitial implements CustomEventInterstitial, InterstitialAdListener {

    private InterstitialAd mInterstitialAd;
    private CustomEventInterstitialListener mInterstitialListener;

    @Override
    public void requestInterstitialAd(CustomEventInterstitialListener customEventInterstitialListener,
                                      Activity activity, String label, String serverParameter,
                                      MediationAdRequest mediationAdRequest, Object o) {
        mInterstitialListener = customEventInterstitialListener;

        // Assuming placement_id is configured in serverParameter.
        String placementId = serverParameter;
        if (placementId == null || placementId.length() == 0) {
            // Invalid placement_id
            mInterstitialListener.onFailedToReceiveAd();
            return;
        }

        mInterstitialAd = new InterstitialAd(activity, placementId);
        mInterstitialAd.setAdListener(this);
        mInterstitialAd.loadAd();
    }

    @Override
    public void showInterstitial() {
        mInterstitialAd.show();
    }

    @Override
    public void destroy() {
        if (mInterstitialAd != null) {
            mInterstitialAd.destroy();
            mInterstitialAd = null;
        }
    }

    @Override
    public void onInterstitialDisplayed(Ad ad) {
        mInterstitialListener.onPresentScreen();
    }

    @Override
    public void onInterstitialDismissed(Ad ad) {
        mInterstitialListener.onDismissScreen();
    }

    @Override
    public void onError(Ad ad, AdError adError) {
        mInterstitialListener.onFailedToReceiveAd();
    }

    @Override
    public void onAdLoaded(Ad ad) {
        mInterstitialListener.onReceivedAd();
    }

    @Override
    public void onAdClicked(Ad ad) {
        mInterstitialListener.onLeaveApplication();
    }
}
