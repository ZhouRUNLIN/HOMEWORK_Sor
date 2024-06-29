#pragma once
#include <iostream>
#include <thread>
#include <chrono>
#include "Color.h"
#include "Sphere.h"
#include "Rayon.h"
#include "Scene.h"
#include "Barrier.h"

using namespace std::chrono_literals;

namespace pr {

    extern int findClosestInter(const Scene & scene, const Rayon & ray);
    extern Color computeColor(const Sphere & obj, const Rayon & ray, const Vec3D & camera, std::vector<Vec3D> & lights);


    class Job {
    public:
        virtual void run () = 0;
        virtual ~Job() {};
    };

// Concrete Job: example
//    class SleepJob : public Job {
//        int calcul (int v) {
//            std::cout << "Computing for arg =" << v << std::endl;
//            // Processing a heavy computation
//            std::this_thread::sleep_for(1s);
//            int ret = v % 255;
//            std::cout << "Obtained for arg =" << arg <<  " result " << ret << std::endl;
//            return ret;
//        }
//        int arg;
//        int * ret;
//    public :
//        SleepJob(int arg, int * ret) : arg(arg), ret(ret) {}
//        void run () {
//            *ret = calcul(arg);
//        }
//        ~SleepJob(){}
//    };



        class PixelJob : public Job {
        private:
            int x, y;
            Color* pixel;
            Scene* scene;
            std::vector<Vec3D>* lights;

        public:

            PixelJob(int x, int y, Color* pixel, Scene* scene, std::vector<Vec3D>* lights)
                    : x(x), y(y), pixel(pixel), scene(scene), lights(lights) {}

            void run() override {

                auto &screenPoint = scene->getScreenPoints()[y][x];
                Rayon ray(scene->getCameraPos(), screenPoint);
                int targetSphere = findClosestInter(*scene, ray);

                if (targetSphere != -1) {
                    const Sphere &obj = *((*scene).begin() + targetSphere);
                    *pixel = computeColor(obj, ray, scene->getCameraPos(), *lights);
                }
            }
        };

    class RowJob : public Job {
    private:
        int y;
        Color* rowPixels;
        Scene* scene;
        std::vector<Vec3D>* lights;
        pr::Barrier& barrier;

    public:
        RowJob(int y, Color* rowPixels, Scene* scene, std::vector<Vec3D>* lights, pr::Barrier& barrier)
                : y(y), rowPixels(rowPixels), scene(scene), lights(lights), barrier(barrier) {}

        void run() override {
            for (int x = 0; x < scene->getWidth(); x++) {
                Color* pixel = &rowPixels[x];
                auto &screenPoint = scene->getScreenPoints()[y][x];
                Rayon ray(scene->getCameraPos(), screenPoint);
                int targetSphere = findClosestInter(*scene, ray);

                if (targetSphere != -1) {
                    const Sphere &obj = *((*scene).begin() + targetSphere);
                    *pixel = computeColor(obj, ray, scene->getCameraPos(), *lights);
                }
            }
            barrier.done();
        }
    };
    }