#!/bin/bash

# Set ANDROID_HOME if not already set
if [ -z "$ANDROID_HOME" ]; then
  export ANDROID_HOME=$HOME/Android/Sdk
fi

# Ensure platform-tools and emulator are in PATH
export PATH=$ANDROID_HOME/platform-tools:$ANDROID_HOME/emulator:$PATH

# Name of the emulator to start
EMULATOR_NAME="Pixel_5_API_33"
SYSTEM_IMAGE="system-images;android-33;google_apis;x86_64"
DEVICE_MODEL="pixel"

# Check if the AVD exists
if ! avdmanager list avd | grep -q "$EMULATOR_NAME"; then
  echo "AVD $EMULATOR_NAME does not exist. Creating it..."
  echo "no" | avdmanager create avd -n "$EMULATOR_NAME" -k "$SYSTEM_IMAGE" --device "$DEVICE_MODEL"
else
  echo "AVD $EMULATOR_NAME already exists."
fi

# Start the emulator
echo "Starting emulator: $EMULATOR_NAME..."
$ANDROID_HOME/emulator/emulator -avd "$EMULATOR_NAME" -no-window -no-snapshot -no-boot-anim > emulator.log 2>&1 &

# Wait for the emulator to boot
echo "Waiting for emulator to boot..."
adb wait-for-device

# Disable animations
echo "Disabling animations..."
adb shell settings put global window_animation_scale 0
adb shell settings put global transition_animation_scale 0
adb shell settings put global animator_duration_scale 0

echo "Emulator $EMULATOR_NAME is ready."
