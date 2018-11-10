package com.magomed.gamzatov.ari


import android.app.ActivityManager
import android.content.Context
import android.content.res.AssetManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.ar.core.AugmentedImageDatabase
import com.google.ar.core.Config
import com.google.ar.core.Session
import com.google.ar.sceneform.ux.ArFragment
import java.io.IOException

private const val TAG = "AugmentedImageFragment"
private const val MIN_OPENGL_VERSION = 3.0
private const val USE_SINGLE_IMAGE = true
private const val SAMPLE_IMAGE_DATABASE = "sample_database.imgdb"
const val DEFAULT_IMAGE_NAME = "rahman_ar4.jpg"
const val DEFAULT_IMAGE_NAME2 = "chaik4.jpg"

class AugmentedImageFragment : ArFragment() {

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        // Check for Sceneform being supported on this device.  This check will be integrated into
        // Sceneform eventually.
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            Log.e(TAG, "Sceneform requires Android N or later")
            SnackbarHelper.getInstance()
                .showError(activity, "Sceneform requires Android N or later")
        }

        val openGlVersionString = (context!!.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager)
            .deviceConfigurationInfo
            .glEsVersion

        if (java.lang.Double.parseDouble(openGlVersionString) < MIN_OPENGL_VERSION) {
            Log.e(TAG, "Sceneform requires OpenGL ES 3.0 or later")
            SnackbarHelper.getInstance()
                .showError(activity, "Sceneform requires OpenGL ES 3.0 or later")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)

        // Turn off the plane discovery since we're only looking for images
        planeDiscoveryController.hide()
        planeDiscoveryController.setInstructionView(null)
        arSceneView.planeRenderer.isEnabled = false
        return view
    }

    override fun getSessionConfiguration(session: Session): Config {
        val config = super.getSessionConfiguration(session)
        if (!setupAugmentedImageDatabase(config, session)) {
            SnackbarHelper.getInstance()
                .showError(activity, "Could not setup augmented image database")
        }
        config.focusMode = Config.FocusMode.AUTO
        return config
    }

    private fun setupAugmentedImageDatabase(config: Config, session: Session): Boolean {
        val assetManager = if (context != null) context!!.assets else null
        if (assetManager == null) {
            Log.e(TAG, "Context is null, cannot intitialize image database.")
            return false
        }

        // There are two ways to configure an AugmentedImageDatabase:
        // 1. Add Bitmap to DB directly
        // 2. Load a pre-built AugmentedImageDatabase
        // Option 2) has
        // * shorter setup time
        // * doesn't require images to be packaged in apk.
        val augmentedImageDatabase: AugmentedImageDatabase = if (USE_SINGLE_IMAGE) {
            val augmentedImageBitmap = loadAugmentedImageBitmap(DEFAULT_IMAGE_NAME, assetManager) ?: return false
            val augmentedImageBitmap2 = loadAugmentedImageBitmap(DEFAULT_IMAGE_NAME2, assetManager) ?: return false
            AugmentedImageDatabase(session).apply {
                addImage(DEFAULT_IMAGE_NAME, augmentedImageBitmap)
                addImage(DEFAULT_IMAGE_NAME2, augmentedImageBitmap2)
            }
            // If the physical size of the image is known, you can instead use:
            //     augmentedImageDatabase.addImage("image_name", augmentedImageBitmap, widthInMeters);
            // This will improve the initial detection speed. ARCore will still actively estimate the
            // physical size of the image as it is viewed from multiple viewpoints.
        } else {
            // This is an alternative way to initialize an AugmentedImageDatabase instance,
            // load a pre-existing augmented image database.
            try {
                context!!.assets.open(SAMPLE_IMAGE_DATABASE)
                    .use { AugmentedImageDatabase.deserialize(session, it) }
            } catch (e: IOException) {
                Log.e(TAG, "IO exception loading augmented image database.", e)
                return false
            }
        }

        config.augmentedImageDatabase = augmentedImageDatabase
        return true
    }

    private fun loadAugmentedImageBitmap(name: String, assetManager: AssetManager): Bitmap? {
        try {
            return assetManager.open(name).use { BitmapFactory.decodeStream(it) }
        } catch (e: IOException) {
            Log.e(TAG, "IO exception loading augmented image bitmap.", e)
        }

        return null
    }
}
