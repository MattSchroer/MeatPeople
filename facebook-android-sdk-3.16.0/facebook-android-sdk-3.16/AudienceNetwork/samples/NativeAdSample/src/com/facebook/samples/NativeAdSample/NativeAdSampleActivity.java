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

package com.facebook.samples.NativeAdSample;

import java.util.*;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.*;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdListener;
import com.facebook.ads.NativeAd;

public class NativeAdSampleActivity extends Activity implements AdListener {

    private TextView nativeAdStatus;
    private LinearLayout nativeAdContainer;
    private ImageView nativeAdIcon;
    private TextView nativeAdTitle;
    private TextView nativeAdBody;
    private ImageView nativeAdImage;
    private TextView nativeAdSocialContext;
    private Button nativeAdCallToAction;
    private RatingBar nativeAdStarRating;
    private Button showNativeAdButton;

    private NativeAd nativeAd;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_native_ad_demo);

        nativeAdStatus = (TextView) findViewById(R.id.nativeAdStatus);
        nativeAdContainer = (LinearLayout) findViewById(R.id.nativeAdContainer);
        nativeAdIcon = (ImageView) findViewById(R.id.nativeAdIcon);
        nativeAdTitle = (TextView) findViewById(R.id.nativeAdTitle);
        nativeAdBody = (TextView) findViewById(R.id.nativeAdBody);
        nativeAdImage = (ImageView) findViewById(R.id.nativeAdImage);
        nativeAdSocialContext = (TextView) findViewById(R.id.nativeAdSocialContext);
        nativeAdCallToAction = (Button) findViewById(R.id.nativeAdCallToAction);
        nativeAdStarRating = (RatingBar) findViewById(R.id.nativeAdStarRating);
        showNativeAdButton = (Button)findViewById(R.id.loadNativeAdButton);

        showNativeAdButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nativeAdStatus.setText("Requesting an ad...");

                // Create a native ad request with a unique placement ID (generate your own on the Facebook app settings).
                // Use different ID for each ad placement in your app.
                nativeAd = new NativeAd(NativeAdSampleActivity.this, "YOUR_PLACEMENT_ID");

                // Set a listener to get notified when the ad was loaded.
                nativeAd.setAdListener(NativeAdSampleActivity.this);

                // When testing on a device, add its hashed ID to force test ads.
                // The hash ID is printed to log cat when running on a device and loading an ad.
                // AdSettings.addTestDevice("THE HASHED ID AS PRINTED TO LOG CAT");

                // Initiate a request to load an ad.
                nativeAd.loadAd();
            }
        });
    }

    @Override
    public void onError(Ad ad, AdError error) {
        nativeAdStatus.setText("Ad failed to load: " + error.getErrorMessage());
    }

    @Override
    public void onAdClicked(Ad ad) {
        Toast.makeText(this, "Ad Clicked", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAdLoaded(Ad ad) {
        if (nativeAd == null || nativeAd != ad) {
            // Race condition, load() called again before last ad was displayed
            return;
        }

        // Unregister last ad
        nativeAd.unregisterView();

        nativeAdStatus.setText("");

        // Create native UI using the ad metadata.

        // Setting the Text
        nativeAdSocialContext.setText(nativeAd.getAdSocialContext());
        nativeAdCallToAction.setText(nativeAd.getAdCallToAction());
        nativeAdTitle.setText(nativeAd.getAdTitle());
        nativeAdBody.setText(nativeAd.getAdBody());

        // Downloading and setting the ad icon.
        NativeAd.Image adIcon = nativeAd.getAdIcon();
        NativeAd.downloadAndDisplayImage(adIcon, nativeAdIcon);

        // Downloading and setting the cover image.
        NativeAd.Image adCoverImage = nativeAd.getAdCoverImage();
        int bannerWidth = adCoverImage.getWidth();
        int bannerHeight = adCoverImage.getHeight();
        WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        int screenWidth = metrics.widthPixels;
        int screenHeight = metrics.heightPixels;
        nativeAdImage.setLayoutParams(new LinearLayout.LayoutParams(
                screenWidth,
                Math.min((int) (((double) screenWidth / (double) bannerWidth) * bannerHeight), screenHeight / 4)
        ));
        NativeAd.downloadAndDisplayImage(adCoverImage, nativeAdImage);

        NativeAd.Rating rating = nativeAd.getAdStarRating();
        if (rating != null) {
            nativeAdStarRating.setNumStars((int) rating.getScale());
            nativeAdStarRating.setRating((float) rating.getValue());
        } else {
            nativeAdStarRating.setVisibility(View.GONE);
        }

        // Wire up the View with the native ad, the whole nativeAdContainer will be clickable
        nativeAd.registerViewForInteraction(nativeAdContainer);

        // Or you can replace the above call with the following function to specify the clickable areas.
        // nativeAd.registerViewForInteraction(nativeAdContainer, Arrays.asList(nativeAdCallToAction, nativeAdImage));

        nativeAdContainer.setVisibility(View.VISIBLE);
    }
}
