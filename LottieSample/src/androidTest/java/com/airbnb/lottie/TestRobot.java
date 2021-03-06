package com.airbnb.lottie;

import android.support.annotation.Nullable;

import com.airbnb.lottie.samples.MainActivity;
import com.facebook.testing.screenshot.Screenshot;
import com.facebook.testing.screenshot.ViewHelpers;

import java.util.concurrent.Semaphore;

class TestRobot {



  static void testLinearAnimation(MainActivity activity, String fileName) {
    testLinearAnimation(activity, fileName, null);
  }

  static void testLinearAnimation(MainActivity activity, String fileName,
      @Nullable String imageAssetsFolder) {
    LottieComposition composition =
        LottieComposition.Factory.fromFileSync(activity, fileName);
    AnimationLinearLayout view = new AnimationLinearLayout(activity);

    view.setImageAssetsFolder(imageAssetsFolder);
    view.setComposition(composition);

    ViewHelpers
        .setupView(view)
        .layout();

    String nameWithoutExtension = fileName
        .substring(0, fileName.lastIndexOf('.'))
        .replace(" ", "_")
        .replace("/", "_");

    Screenshot.snap(view)
        .setGroup(fileName)
        .setName(nameWithoutExtension)
        .record();
  }

  static void testLinearAnimationWithScale(MainActivity activity, String fileName) {
    testLinearAnimationWithScale(activity, fileName, null);
  }

  static final float SCALE = 0.5f;

  static void testLinearAnimationWithScale(MainActivity activity, String fileName,
      @Nullable String imageAssetsFolder) {
    LottieComposition composition =
        LottieComposition.Factory.fromFileSyncScaled(activity, fileName, SCALE);
    ScaledAnimationLinearLayout view = new ScaledAnimationLinearLayout(activity);

    view.setImageAssetsFolder(imageAssetsFolder);
    view.setComposition(composition);

    ViewHelpers
        .setupView(view)
        .layout();

    String nameWithoutExtension = fileName
        .substring(0, fileName.lastIndexOf('.'))
        .replace(" ", "_")
        .replace("/", "_")
        .concat("_scaled");
    Screenshot.snap(view)
        .setGroup(fileName)
        .setName(nameWithoutExtension)
        .record();
  }


  static void testChangingCompositions(MainActivity activity, String firstFile, String secondFile) {
    final AnimationLinearLayout view = new AnimationLinearLayout(activity);
    loadCompositionOnView(view, null, firstFile);
    recordScreenshots(view, "test_changing_compositions_" + firstFile);

    loadCompositionOnView(view, null, secondFile);
    recordScreenshots(view, "test_changing_compositions_" + secondFile);

    cleanUpView(view, activity);
  }

  static void testSettingSameComposition(MainActivity activity, String fileName) {
    final AnimationLinearLayout view = new AnimationLinearLayout(activity);
    loadCompositionOnView(view, null, fileName);
    recordScreenshots(view, "same_composition_first_run_" + fileName);

    loadCompositionOnView(view, null, fileName);
    recordScreenshots(view, "same_composition_second_run_" + fileName);

    cleanUpView(view, activity);
  }

  private static void loadCompositionOnView(AnimationLinearLayout view, String imageAssetsFolder,
      String fileName) {

    LottieComposition composition =
        LottieComposition.Factory.fromFileSync(view.getContext(), fileName);

    view.setImageAssetsFolder(imageAssetsFolder);
    view.setComposition(composition);

    ViewHelpers
        .setupView(view)
        .layout();
  }

  private static void recordScreenshots(AnimationLinearLayout view, String fileName) {
    String nameWithoutExtension = fileName
        .substring(0, fileName.lastIndexOf('.'))
        .replace(" ", "_")
        .replace("/", "_");
    Screenshot.snap(view)
        .setGroup(fileName)
        .setName(nameWithoutExtension)
        .record();
  }

  private static void cleanUpView(final AnimationLinearLayout view, MainActivity activity) {
    final Semaphore semaphore = new Semaphore(1);
    activity.runOnUiThread(new Runnable() {
      @Override public void run() {
        view.recycleBitmaps();
        semaphore.release();
      }
    });

    try {
      semaphore.acquire();
    } catch (InterruptedException e) {
      // Do nothing.
    }
  }


}
