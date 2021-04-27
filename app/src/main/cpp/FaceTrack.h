//
// Created by Zach on 2021/4/26.
//

#ifndef OPENGLCAMERADEMO_FACETRACK_H
#define OPENGLCAMERADEMO_FACETRACK_H

#include <seeta//FaceLandmarker.h>
#include <opencv2/opencv.hpp>
#include <opencv2/imgproc/types_c.h>

using namespace cv;
using namespace seeta;
using namespace std;

class CascadeDetectorAdapter : public DetectionBasedTracker::IDetector {
public:
    CascadeDetectorAdapter(Ptr<CascadeClassifier> detector) : IDetector(), Detector(detector) {
        CV_Assert(detector);
    }

    void detect(const Mat &Image, vector<cv::Rect> &objects) {
        Detector->detectMultiScale(Image, objects, scaleFactor, minNeighbours, 0, minObjSize, maxObjSize);
    }

    virtual ~CascadeDetectorAdapter() {

    }

private:
    CascadeDetectorAdapter();
    Ptr<CascadeClassifier> Detector;

};


class FaceTrack {
public:
    FaceTrack(const char* faceModel, const char* landmarkerModel);

    ~FaceTrack();

    void stop();
    void run();
    void process(Mat src, cv::Rect &face, vector<SeetaPointF>& points);


private:
    DetectionBasedTracker *tracker;
    FaceLandmarker *landmarker;
};


#endif //OPENGLCAMERADEMO_FACETRACK_H
