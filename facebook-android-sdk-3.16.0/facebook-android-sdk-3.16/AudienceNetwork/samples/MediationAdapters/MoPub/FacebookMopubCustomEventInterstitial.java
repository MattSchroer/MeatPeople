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
import android.content.Context;
import com.mopub.mobileads.CustomEventInterstitial;
import com.mopub.mobileads.MoPubErrorCode;

import java.util.Map;

/**
 * FacebookMopubCustomEventInterstitial is a MoPub interstitial adapter for InterstitialAd.
 * <p/>
 * Compatible with mopub-android-sdk version 1.17.3.1
 * <p/>
 * Declare com.facebook.ads.InterstitialAdActivity in AndroidManifest.xml
 * with android:configChanges="keyboardHidden|orientation"
 */
public class FacebookMopubCustomEventInterstitial extends CustomEventInterstitial implements InterstitialAdListener {

    private InterstitialAd mInterstitialAd;
    private CustomEventInterstitialListener mInterstitialListener;

    @Override
    protected void loadInterstitial(Context context, CustomEventInterstitialListener customEventInterstitialListener, Map<String, Object> localExtras, Map<String, String> serverExtras) {
        mInterstitialListener = customEventInterstitialListener;

        if (!(context instanceof Activity)) {
            mInterstitialListener.onInterstitialFailed(MoPubErrorCode.ADAPTER_CONFIGURATION_ERROR);
        }

        // Assuming placement_id is configured in serverExtras with key placement_id.
        String placementId = serverExtras.get("placement_id");
        if (placementId == null || placementId.length() == 0) {
            // Invalid placement_id
            mInterstitialListener.onInterstitialFailed(MoPubErrorCode.ADAPTER_CONFIGURATION_ERROR);
            return;
        }

        mInterstitialAd = new InterstitialAd(context, placementId);
        mInterstitialAd.setAdListener(this);
        mInterstitialAd.loadAd();
    }

    @Override
    protected void showInterstitial() {
        mInterstitialAd.show();
    }

    @Override
    protected void onInvalidate() {
        if (mInterstitialAd != null) {
            mInterstitialAd.destroy();
            mInterstitialAd = null;
        }
    }

    @Override
    public void onError(Ad ad, AdError error) {
        if (error.getErrorCode() == AdError.NO_FILL.getErrorCode()) {
            mInterstitialListener.onInterstitialFailed(MoPubErrorCode.NETWORK_NO_FILL);
        } else if (error == AdError.INTERNAL_ERROR) {
            mInterstitialListener.onInterstitialFailed(MoPubErrorCode.NETWORK_INVALID_STATE);
        } else {
            mInterstitialListener.onInterstitialFailed(MoPubErrorCode.UNSPECIFIED);
        }
    }

    @Override
    public void onAdLoaded(Ad ad) {
        mInterstitialListener.onInterstitialLoaded();
    }

    @Override
    public void onAdClicked(Ad ad) {
        mInterstitialListener.onInterstitialClicked();
    }

    @Override
    public void onInterstitialDisplayed(Ad ad) {
        mInterstitialListener.onInterstitialShown();
    }

    @Override
    public void onInterstitialDismissed(Ad ad) {
        mInterstitialListener.onInterstitialDismissed();
    }
}
