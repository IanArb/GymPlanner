#!/bin/bash

# Set ANDROID_HOME if not already set
if [ -z "$ANDROID_HOME" ]; then
  export ANDROID_HOME=$HOME/Android/Sdk
fi

# Ensure platform-tools is in PATH
export PATH=$ANDROID_HOME/platform-tools:$ANDROID_HOME/emulator:$PATH

# Name of the emulator to start
EMULATOR_NAME="Pixel_5_API_33"

# Start the emulator
echo "Starting emulator: $EMULATOR_NAME..."
$ANDROID_HOME/emulator/emulator -avd "$EMULATOR_NAME" -no-window -no-snapshot -no-boot-anim &

# Wait for the emulator to boot
echo "Waiting for emulator to boot..."
adb wait-for-device

# Disable animations
echo "Disabling animations..."
adb shell settings put global window_animation_scale 0
adb shell settings put global transition_animation_scale 0
adb shell settings put global animator_duration_scale 0

echo "Emulator $EMULATOR_NAME is ready."
