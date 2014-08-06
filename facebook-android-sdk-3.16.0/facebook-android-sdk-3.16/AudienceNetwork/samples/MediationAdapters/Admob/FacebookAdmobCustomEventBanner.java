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
import com.google.ads.AdSize;
import com.google.ads.mediation.MediationAdRequest;
import com.google.ads.mediation.customevent.CustomEventBanner;
import com.google.ads.mediation.customevent.CustomEventBannerListener;

/**
 * FacebookAdmobCustomEventBanner is an admob adapter for AdView.
 * <p/>
 * Compatible with Google Mobile Ads SDK version 6.4.1
 */
public class FacebookAdmobCustomEventBanner implements CustomEventBanner, AdListener {

    private AdView mAdView;
    private CustomEventBannerListener mBannerListener;

    @Override
    public void requestBannerAd(CustomEventBannerListener customEventBannerListener, Activity activity,
                                String label, String serverParameter, AdSize adSize,
                                MediationAdRequest mediationAdRequest, Object o) {
        mBannerListener = customEventBannerListener;

        // Assuming placement_id is configured in serverParameter.
        String placementId = serverParameter;
        if (placementId == null || placementId.length() == 0) {
            // Invalid placement_id
            mBannerListener.onFailedToReceiveAd();
            return;
        }

        mAdView = new AdView(activity, placementId, com.facebook.ads.AdSize.BANNER_320_50);
        mAdView.setAdListener(this);
        mAdView.loadAd();
    }

    @Override
    public void destroy() {
        if (mAdView != null) {
            mAdView.destroy();
            mAdView = null;
        }
    }

    @Override
    public void onError(Ad ad, AdError adError) {
        mBannerListener.onFailedToReceiveAd();
    }

    @Override
    public void onAdLoaded(Ad ad) {
        mBannerListener.onReceivedAd(mAdView);
    }

    @Override
    public void onAdClicked(Ad ad) {
        mBannerListener.onClick();
    }
}
