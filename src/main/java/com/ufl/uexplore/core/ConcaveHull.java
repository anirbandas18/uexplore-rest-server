package com.ufl.uexplore.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

import org.geojson.LngLatAlt;

import javafx.util.Pair;

public class ConcaveHull {
	
	
    private Double euclideanDistance(LngLatAlt a, LngLatAlt b) {
        return Math.sqrt(Math.pow(a.getLongitude() - b.getLongitude(), 2) + Math.pow(a.getLatitude() - b.getLatitude(), 2));
    }

    private List<LngLatAlt> kNearestNeighbors(List<LngLatAlt> l, LngLatAlt q, Integer k) {
        List<Pair<Double, LngLatAlt>> nearestList = new ArrayList<>();
        for (LngLatAlt o : l) {
            nearestList.add(new Pair<>(euclideanDistance(q, o), o));
        }

        Collections.sort(nearestList, new Comparator<Pair<Double, LngLatAlt>>() {
            @Override
            public int compare(Pair<Double, LngLatAlt> o1, Pair<Double, LngLatAlt> o2) {
                return o1.getKey().compareTo(o2.getKey());
            }
        });

        List<LngLatAlt> result = new ArrayList<>();

        for (int i = 0; i < Math.min(k, nearestList.size()); i++) {
            result.add(nearestList.get(i).getValue());
        }

        return result;
    }

    private LngLatAlt findMinYLngLatAlt(List<LngLatAlt> l) {
        Collections.sort(l, new Comparator<LngLatAlt>() {
            @Override
            public int compare(LngLatAlt o1, LngLatAlt o2) {
            	Double lat1 = o1.getLatitude();
            	Double lat2 = o2.getLatitude();
                return lat1.compareTo(lat2);
            }
        });
        return l.get(0);
    }

    private Double calculateAngle(LngLatAlt o1, LngLatAlt o2) {
        return Math.atan2(o2.getLatitude() - o1.getLatitude(), o2.getLongitude() - o1.getLongitude());
    }

    private Double angleDifference(Double a1, Double a2) {
        // calculate angle difference in clockwise directions as radians
        if ((a1 > 0 && a2 >= 0) && a1 > a2) {
            return Math.abs(a1 - a2);
        } else if ((a1 >= 0 && a2 > 0) && a1 < a2) {
            return 2 * Math.PI + a1 - a2;
        } else if ((a1 < 0 && a2 <= 0) && a1 < a2) {
            return 2 * Math.PI + a1 + Math.abs(a2);
        } else if ((a1 <= 0 && a2 < 0) && a1 > a2) {
            return Math.abs(a1 - a2);
        } else if (a1 <= 0 && 0 < a2) {
            return 2 * Math.PI + a1 - a2;
        } else if (a1 >= 0 && 0 >= a2) {
            return a1 + Math.abs(a2);
        } else {
            return 0.0;
        }
    }

    private List<LngLatAlt> sortByAngle(List<LngLatAlt> l, final LngLatAlt q, final Double a) {
        // Sort by angle descending
        Collections.sort(l, new Comparator<LngLatAlt>() {
            @Override
            public int compare(final LngLatAlt o1, final LngLatAlt o2) {
                Double a1 = angleDifference(a, calculateAngle(q, o1));
                Double a2 = angleDifference(a, calculateAngle(q, o2));
                return a2.compareTo(a1);
            }
        });
        return l;
    }

    private Boolean intersect(LngLatAlt l1p1, LngLatAlt l1p2, LngLatAlt l2p1, LngLatAlt l2p2) {
        // calculate part equations for line-line intersection
        Double a1 = l1p2.getLatitude() - l1p1.getLatitude();
        Double b1 = l1p1.getLongitude() - l1p2.getLongitude();
        Double c1 = a1 * l1p1.getLongitude() + b1 * l1p1.getLatitude();
        Double a2 = l2p2.getLatitude() - l2p1.getLatitude();
        Double b2 = l2p1.getLongitude() - l2p2.getLongitude();
        Double c2 = a2 * l2p1.getLongitude() + b2 * l2p1.getLatitude();
        // calculate the divisor
        Double tmp = (a1 * b2 - a2 * b1);

        // calculate intersection point x coordinate
        Double pX = (c1 * b2 - c2 * b1) / tmp;

        // check if intersection x coordinate lies in line line segment
        if ((pX > l1p1.getLongitude() && pX > l1p2.getLongitude()) || (pX > l2p1.getLongitude() && pX > l2p2.getLongitude())
                || (pX < l1p1.getLongitude() && pX < l1p2.getLongitude()) || (pX < l2p1.getLongitude() && pX < l2p2.getLongitude())) {
            return false;
        }

        // calculate intersection point y coordinate
        Double pY = (a1 * c2 - a2 * c1) / tmp;

        // check if intersection y coordinate lies in line line segment
        if ((pY > l1p1.getLatitude() && pY > l1p2.getLatitude()) || (pY > l2p1.getLatitude() && pY > l2p2.getLatitude())
                || (pY < l1p1.getLatitude() && pY < l1p2.getLatitude()) || (pY < l2p1.getLatitude() && pY < l2p2.getLatitude())) {
            return false;
        }

        return true;
    }

    private boolean pointInPolygon(LngLatAlt p, List<LngLatAlt> pp) {
        boolean result = false;
        for (int i = 0, j = pp.size() - 1; i < pp.size(); j = i++) {
            if ((pp.get(i).getLatitude() > p.getLatitude()) != (pp.get(j).getLatitude() > p.getLatitude()) &&
                    (p.getLongitude() < (pp.get(j).getLongitude() - pp.get(i).getLongitude()) * (p.getLatitude() - pp.get(i).getLatitude()) / (pp.get(j).getLatitude() - pp.get(i).getLatitude()) + pp.get(i).getLongitude())) {
                result = !result;
            }
        }
        return result;
    }
    
    public List<LngLatAlt> calculate(Collection<LngLatAlt> pointArrayList, Integer k) {

        // the resulting concave hull
        List<LngLatAlt> concaveHull = new ArrayList<>();

        // optional remove duplicates
        HashSet<LngLatAlt> set = new HashSet<>(pointArrayList);
        List<LngLatAlt> pointArraySet = new ArrayList<>(set);

        // k has to be greater than 3 to execute the algorithm
        Integer kk = Math.max(k, 3);

        // return LngLatAlts if already Concave Hull
        if (pointArraySet.size() < 3) {
            return pointArraySet;
        }

        // make sure that k neighbors can be found
        kk = Math.min(kk, pointArraySet.size() - 1);

        // find first point and remove from point list
        LngLatAlt firstLngLatAlt = findMinYLngLatAlt(pointArraySet);
        concaveHull.add(firstLngLatAlt);
        LngLatAlt currentLngLatAlt = firstLngLatAlt;
        pointArraySet.remove(firstLngLatAlt);

        Double previousAngle = 0.0;
        Integer step = 2;

        while ((currentLngLatAlt != firstLngLatAlt || step == 2) && pointArraySet.size() > 0) {

            // after 3 steps add first point to dataset, otherwise hull cannot be closed
            if (step == 5) {
                pointArraySet.add(firstLngLatAlt);
            }

            // get k nearest neighbors of current point
            List<LngLatAlt> kNearestLngLatAlts = kNearestNeighbors(pointArraySet, currentLngLatAlt, kk);

            // sort points by angle clockwise
            List<LngLatAlt> clockwiseLngLatAlts = sortByAngle(kNearestLngLatAlts, currentLngLatAlt, previousAngle);

            // check if clockwise angle nearest neighbors are candidates for concave hull
            Boolean its = true;
            int i = -1;
            while (its && i < clockwiseLngLatAlts.size() - 1) {
                i++;

                int lastLngLatAlt = 0;
                if (clockwiseLngLatAlts.get(i) == firstLngLatAlt) {
                    lastLngLatAlt = 1;
                }

                // check if possible new concave hull point intersects with others
                int j = 2;
                its = false;
                while (!its && j < concaveHull.size() - lastLngLatAlt) {
                    its = intersect(concaveHull.get(step - 2), clockwiseLngLatAlts.get(i), concaveHull.get(step - 2 - j), concaveHull.get(step - 1 - j));
                    j++;
                }
            }

            // if there is no candidate increase k - try again
            if (its) {
                return calculate(pointArrayList, k + 1);
            }

            // add candidate to concave hull and remove from dataset
            currentLngLatAlt = clockwiseLngLatAlts.get(i);
            concaveHull.add(currentLngLatAlt);
            pointArraySet.remove(currentLngLatAlt);

            // calculate last angle of the concave hull line
            previousAngle = calculateAngle(concaveHull.get(step - 1), concaveHull.get(step - 2));

            step++;

        }

        // Check if all points are contained in the concave hull
        Boolean insideCheck = true;
        int i = pointArraySet.size() - 1;

        while (insideCheck && i > 0) {
            insideCheck = pointInPolygon(pointArraySet.get(i), concaveHull);
            i--;
        }

        // if not all points inside -  try again
        if (!insideCheck) {
            return calculate(pointArrayList, k + 1);
        } else {
        	return concaveHull;
        }

    }

}


