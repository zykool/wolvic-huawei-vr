# Wolvic VR180 Pitch Adjustment Feature - Implementation Summary

## Overview
This modification adds a pitch adjustment feature for VR180° videos in Wolvic, allowing users to watch VR180 content while lying down by adjusting the vertical viewing angle (-90° to +90°).

## Files Modified

### C++ Layer (Native Code)

#### 1. VRVideo.h
- Added `SetPitch(float aPitch)` method declaration
- Added `GetPitch()` method declaration
- Added `Is180Projection()` method declaration

#### 2. VRVideo.cpp
- Added `mPitch` state variable in State struct (line 44)
- Modified `create180LayerToggle()` to combine pitch rotation with yaw rotation:
  - Creates combined rotation matrix: `pitchRotation * yawRotation`
  - Pitch rotates around X-axis (vertical adjustment)
  - Yaw rotates around Y-axis (centers the 180° view)
- Implemented `SetPitch()` - updates pitch dynamically for 180 videos
- Implemented `GetPitch()` - returns current pitch value
- Implemented `Is180Projection()` - checks if current projection is 180°

#### 3. BrowserWorld.h
- Added `SetVRVideoPitch(const float aPitch)` method
- Added `GetVRVideoPitch()` method  
- Added `IsVRVideo180()` method

#### 4. BrowserWorld.cpp
- Implemented `SetVRVideoPitch()` - forwards to VRVideo::SetPitch()
- Implemented `GetVRVideoPitch()` - returns current pitch from VRVideo
- Implemented `IsVRVideo180()` - checks if current VR video is 180° projection
- Added JNI methods:
  - `setVRVideoPitchNative(jfloat)`
  - `getVRVideoPitchNative()` returns jfloat
  - `isVRVideo180Native()` returns jboolean

### Java Layer

#### 5. WidgetManagerDelegate.java
- Added interface methods:
  - `void setVRVideoPitch(float aPitch)`
  - `float getVRVideoPitch()`
  - `boolean isVRVideo180()`

#### 6. VRBrowserActivity.java
- Implemented WidgetManagerDelegate interface methods:
  - `setVRVideoPitch()` - calls native via queueRunnable
  - `getVRVideoPitch()` - returns native pitch value
  - `isVRVideo180()` - returns whether current video is 180°
- Native method declarations (already added):
  - `private native void setVRVideoPitchNative(float aPitch)`
  - `private native float getVRVideoPitchNative()`
  - `private native boolean isVRVideo180Native()`

### UI Layer

#### 7. PitchControl.java (NEW FILE)
- New vertical slider control for pitch adjustment
- Range: -90° to +90°
- Delegate interface for pitch change callbacks
- Similar structure to VolumeControl

#### 8. pitch_control.xml (NEW FILE)
- Layout for pitch slider control
- Vertical SeekBar with rotation=270
- Uses existing drawable resources

#### 9. media_controls.xml
- Added pitch button (`mediaPitchButton`) in control row
- Added PitchControl widget (`pitchControl`) as popup slider
- Uses `ic_icon_move_up` as button icon

#### 10. strings.xml
- Added `video_controls_pitch` string resource: "Pitch Adjustment"

#### 11. MediaControlsWidget.java
- Added PitchControl import
- Added pitch control handler variables (mPitchCtrlHandler, mHidePitchSlider, mPitchCtrlRunnable)
- Added pitch button click handler
- Added PitchControl delegate implementation
- Added hover listeners for pitch button and control
- Added `startPitchCtrlHandler()` and `stopPitchCtrlHandler()` methods
- Added `setPitchButtonEnabled()` method

## Usage Instructions

1. Open a VR180° video in Wolvic
2. Select VR180 projection mode (180, 180 Stereo LR, or 180 Stereo TB)
3. Click the pitch adjustment button (up arrow icon) in media controls
4. Drag the vertical slider to adjust viewing angle:
   - Up (+): Look upward - useful when lying on back
   - Middle (0): Normal viewing position
   - Down (-): Look downward - useful when lying face down

## Technical Notes

- Pitch adjustment only works for 180° projection modes
- The rotation is applied as a matrix multiplication: `pitchRotation * yawRotation`
- Pitch range is clamped to ±90° in the UI
- The feature uses existing Wolvic architecture patterns (similar to volume control)