# SkinSavvy – AI-Based Skin Cancer Detection App
<img width="1371" height="427" alt="image" src="https://github.com/user-attachments/assets/81c5a013-84be-4855-b9c8-9a20ff5c28a1" />
SkinSavvy is an Android application that uses Convolutional Neural Networks (CNN) and TensorFlow Lite to perform early detection of skin cancer (melanoma) from images. The app allows users to analyze skin lesions using the camera or gallery and provides a fast, lightweight diagnostic assistant.

## Features
- Capture or upload skin images  
- Image cropper built-in  
- CNN model using TensorFlow Lite  
- Detect Cancer vs Non-Cancer  
- Confidence score display  
- Save prediction history  
- About page with basic skin-cancer information  
- Lightweight and fast inference  

## Machine Learning Model
The model is trained using the HAM10000 Skin Cancer Dataset:
https://www.kaggle.com/datasets/kmader/skincancermnistham10000

Model details:
- Architecture: Custom CNN or MobileNetV2  
- Input size: 224x224x3  
- Output: 2 classes (Cancer, Non-Cancer)  
- Converted to TensorFlow Lite (.tflite)  
- Supports quantization  

## Tech Stack
- Android (Kotlin / Java)  
- TensorFlow Lite  
- CNN Model  
- Image Cropper Library  
- Material Design 3  
- Jetpack Components  

## How It Works
1. User selects or takes a skin image  
2. Image is cropped to focus on the lesion  
3. TensorFlow Lite model analyzes the image  
4. The app shows prediction (Cancer / Non-Cancer) and confidence score  
5. Users can save results into History  

## Disclaimer
SkinSavvy is not a medical diagnostic tool.  
It provides initial screening only.  
Users should consult medical professionals for official diagnosis.

## License
MIT License.
