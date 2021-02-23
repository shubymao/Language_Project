import time


def main():
    startTime = time.time()
    n = 10
    val = [None]*n
    cnt = [0]*n
    print(f'Starting CSP for N = {n}')
    result = dfs(val, cnt, 0, n)  # silly_dfs(val, 0) #
    if result:
        print(f'First Found Result: {result}')
    else:
        print('No solution found.')
    duration = time.time()-startTime
    print(f'Program finished. Duration: {duration}')


def dfs(val, cnt, ind, remain):
    if ind == len(cnt) and remain == 0:
        return val
    print(str(val).replace("None", "_"))
    for i in range(0, min(len(cnt), remain+1)):
        val[ind] = i
        if cnt[ind] > i:
            continue
        if ind >= i and cnt[i] >= val[i]:
            continue
        cnt[i] += 1
        if res := dfs(val, cnt, ind+1, remain - i):
            return res
        cnt[i] -= 1
    val[ind] = None
    return False


def silly_dfs(val, ind):
    print(str(val).replace("None", "_"))
    if not check(val, ind):
        return False
    if ind == len(val):
        if not finalCheck(val):
            return False
        return val

    for i in range(0, len(val)):
        val[ind] = i
        if res := silly_dfs(val, ind+1):
            return res
    val[ind] = None
    return False


def finalCheck(val):
    cnt = [0]*10
    for i in range(10):
        cnt[val[i]] += 1
    for i in range(10):
        if cnt[i] != val[i]:
            return False
    return True


def check(val, ind):
    cnt = [0]*10
    for i in range(ind):
        cnt[val[i]] += 1
    for i in range(ind):
        if cnt[i] > val[i]:
            return False
    return True


if __name__ == '__main__':
    main()
