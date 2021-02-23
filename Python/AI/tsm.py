import random
import math
import numpy
import time
from copy import deepcopy


INT_MAX = 1000000000
EPSILON = 0.0000000000001


def main():
    log = open('tsm-log.txt', 'w+')
    
    res = simulateTSP(100, 7, True, log)
    (optCosts, randCosts, greedCosts, randCnt, greedCnt) = res
    plog(log, f'Random Optimal: {randCnt}, Greedy Optimal {greedCnt}')
    printStats(optCosts, log, 'Optimal Costs')
    printStats(randCosts, log, 'Baseline Costs')
    printStats(greedCosts, log, 'Greedy Costs')
    
    res = simulateTSP(100, 100, False, log)
    (optCosts, randCosts, greedCosts, randCnt, greedCnt) = res
    printStats(randCosts, log, 'Baseline Costs')
    printStats(greedCosts, log, 'Greedy Costs')
    print('Test Completed See tms-log.txt for result')
    log.close()


def simulateTSP(times=100, n=7, bFlag=True, log = None):
    optCosts, randCosts, greedCosts = [], [], []
    randCnt, greedCnt , startTime= 0, 0, time.time()
    plog(log,f'Performing {n} points simulations {times} times')
    for i in range(times):
        printProgressBar(i+1,times,'Progress: ')
        pts = genPts(n)  # generate n random points
        # perform randomize to get random path
        rand = deepcopy(pts)
        random.shuffle(rand)
        randCosts.append(totalDistance(rand))
        # perform greedy on randomized path
        greedy = greedySearch(rand)
        greedCosts.append(totalDistance(greedy))
        if bFlag:
            # perform brute force to get optimal path
            opt = bruteForce(deepcopy(pts))
            optCosts.append(totalDistance(opt))
            # sanity check
            check(optCosts, greedCosts)
            # update counter
            if close(optCosts[-1], randCosts[-1]):
                randCnt += 1
            if close(optCosts[-1], greedCosts[-1]):
                greedCnt += 1
    duration = time.time() - startTime
    plog(log,f'Simulation Completed. Time Elapsed: {duration}s')
    return (optCosts, randCosts, greedCosts, randCnt, greedCnt)


def bruteForce(pts):
    n = len(pts)
    minCost = INT_MAX
    optimal = None

    def helper(points, i):
        if i == n-1:
            nonlocal minCost, optimal
            cost = totalDistance(points)
            if minCost > cost:
                minCost = cost
                optimal = deepcopy(points)
            return
        for j in range(i, n):
            swap(points, i, j)
            helper(points, i+1)
            swap(points, i, j)

    helper(pts, 0)
    return optimal


def greedySearch(pts, update = None):
    n = len(pts)
    mp = []
    memo = {}
    for i in range(n):
        mp.append(i)
        # imagine mapping where adjecent cell is connected
        # i.e pts[mp[i]] <-> pts[mp[(i+1)%n]]
    while True:
        saving, bestI, bestJ = 0, -1, -1
        for i in range(n-2):
            for j in range(i+2, n):
                if (j+1) % n == i:
                    continue
                curCost = edgeDistance(mp, memo, pts, i, i+1)
                curCost = curCost + edgeDistance(mp, memo, pts, j, (j+1) % n)
                swapCost = edgeDistance(mp, memo, pts, i, j)
                swapCost = swapCost + edgeDistance(mp, memo, pts, i+1, (j+1) % n)
                if curCost - swapCost > saving:
                    saving, bestI, bestJ = curCost - swapCost, i, j
        if saving == 0:
            break  # local max, no better solution found
        # reverse the best determined section
        section = mp[bestI+1:bestJ+1]
        section.reverse()
        mp[bestI+1:bestJ+1] = section
        if update is not None: update(translateMapping(mp, pts))

    # construct the order of the city
    pts = translateMapping(mp, pts)
    return pts


def translateMapping(mp, pts):
    newPts = []
    for i in range(len(pts)):
        newPts.append(pts[mp[i]])
    return newPts


def printStats(arr, log, title):
    mi, mx, mn, st= min(arr), max(arr), numpy.mean(arr), numpy.std(arr)
    plog(log, f'{ title }---- min:{mi}, max: {mx}, mean:{mn} std: {st}')


def check(best, greedy):
    if best[-1] - greedy[-1] > EPSILON:
        # sanity check make sure optimal is always better
        msg = f'Greedy more optimal than brute force'
        log.write(msg + '\n')
        raise Exception(msg)


def edgeDistance(mp, memo, pts, i, j):
    if(mp[i] in memo and mp[j] in memo[mp[i]]):
        return memo[mp[i]][mp[j]]
    totalDist = distance(pts[mp[i]], pts[mp[j]])
    if(i not in memo): 
        memo[mp[i]] = {}
    memo[mp[i]][mp[j]] = totalDist
    return totalDist


def plog(log, msg):
    print(msg)
    log.write(msg+'\n')


def totalDistance(pts):
    totalDist = 0
    for i in range(1, len(pts)):
        totalDist = totalDist + distance(pts[i-1], pts[i])
    totalDist = totalDist + distance(pts[0], pts[len(pts)-1])
    return totalDist


def distance(cord1, cord2):
    return math.hypot(cord2[0] - cord1[0], cord2[1] - cord1[1])


def swap(arr, i, j):
    arr[i], arr[j] = arr[j], arr[i]


def close(val1, val2):
    return abs(val1 - val2) < EPSILON


def genPts(n):
    pts = []
    for i in range(n):
        x = random.uniform(0, 1)
        y = random.uniform(0, 1)
        pts.append([x, y])
    return pts

def printProgressBar(iteration, total, prefix = '', decimals = 1, length = 80, fill = '█', printEnd = "\r"):
    percent = ("{0:." + str(decimals) + "f}").format(100 * (iteration / float(total)))
    filledLength = int(length * iteration // total)
    bar = fill * filledLength + '-' * (length - filledLength)
    print(f'\r{prefix} |{bar}| {percent}%', end = printEnd)
    if iteration == total: 
        print()

if __name__ == '__main__':
    main()
