from queue import Queue, PriorityQueue
from copy import deepcopy
from math import factorial
# A look up table of the possible move
dirs = [[1, 0], [0, 1], [-1, 0], [0, -1]]
MAX_DEPTH = factorial(6)
FOUND_MESSAGE = 'Reached final state \n'

def main():
    # where first 3 the top and second represent bottom
    print('Initalized state and final state')
    print('---------------------------------\n')
    initState = [[1, 4, 2], [5, 3, 0]]
    finalState = [[0, 1, 2], [5, 4, 3]]  # the desirable final state
    print('Performing breath first search')
    path = bfs(deepcopy(initState), finalState)
    savePath(path, 'bfs-log.txt')
    print('Performing uniform cost search')
    path = ucs(deepcopy(initState), finalState)
    savePath(path, 'ucs-log.txt')
    print('Performing depth first search')
    path = dfs(deepcopy(initState), finalState)
    savePath(path, 'dfs-log.txt')
    print('Performing iterative deepening')
    path = iters(deepcopy(initState), finalState)
    savePath(path, 'iters-log.txt')
    print('Search completed: refer to each log for the path')


def dfs(init, final, maxDepth=MAX_DEPTH):
    def helper(cur, final, visited, row, col, depth):
        state = encode(cur)
        if state in visited or depth > maxDepth:
            return False
        if(state == final):
            return [decode(final)]
        visited.add(state)
        for [nrow, ncol, _] in getMoves(cur, row, col):
            swap(cur, row, col, nrow, ncol)
            path = helper(cur, final, visited, nrow, ncol, depth + 1)
            if path:
                path.append(decode(state))
                return path
            swap(cur, row, col, nrow, ncol)
        return False
    visited = set()
    res = helper(init, encode(final), visited, 1, 2, 0)
    if res:
        res.reverse()
    return res


def bfs(init, final):
    q = Queue()
    visited = {}
    visited[encode(init)] = -1
    final = encode(final)
    q.put([init, 1, 2])
    while not q.empty():
        [cur, row, col] = q.get()
        state = encode(cur)
        if(state == final):
            return getPath(visited, final)
        for [nrow, ncol, _] in getMoves(cur, row, col):
            swap(cur, row, col, nrow, ncol)
            if encode(cur) not in visited:
                visited[encode(cur)] = state
                q.put([deepcopy(cur), nrow, ncol])
            swap(cur, row, col, nrow, ncol)
    return False


def ucs(init, final):
    pq = PriorityQueue()
    visited = {}
    visited[encode(init)] = -1
    final = encode(final)
    pq.put([0, init, 1, 2])
    while not pq.empty():
        [cost, cur, row, col] = pq.get()
        state = encode(cur)
        if(state == final):
            return getPath(visited, final)
        for [nrow, ncol, _] in getMoves(cur, row, col):
            swap(cur, row, col, nrow, ncol)
            if encode(cur) not in visited:
                visited[encode(cur)] = state
                pq.put([cost+1, deepcopy(cur), nrow, ncol])
            swap(cur, row, col, nrow, ncol)
    return False


def iters(init, final):
    res = []
    depthFound = 0
    for depth in range(MAX_DEPTH):
        res = dfs(deepcopy(init), final, depth)
        if res:
            res.insert(0, f'Found path with depth {depth}')
            return res
    return False


def getMoves(state, row, col):
    moves = []
    for d in dirs:
        nr = row+d[0]
        nc = col+d[1]
        if nr < 0 or nc < 0 or nr >= 2 or nc >= 3:
            continue
        moves.append([nr, nc, state[nr][nc]])
    moves = sorted(moves, key = lambda x: x[2])
    return moves


def getPath(visited, final):
    states = [decode(final)]
    cur = final
    while(visited[cur] != -1):
        states.append(decode(visited[cur]))
        cur = visited[cur]
    states.reverse()
    return states


def swap(grid, r1, c1, r2, c2):
    grid[r1][c1], grid[r2][c2] = grid[r2][c2], grid[r1][c1]
    return grid


def encode(state):
    hashCode = ''
    for level in state:
        for col in level:
            hashCode += str(col)
    return hashCode


def decode(hashCode):
    grid = [[0, 0, 0], [0, 0, 0]]
    for i in range(2):
        for j in range(3):
            grid[i][j] = int(hashCode[i*3+j])
    return grid


def savePath(paths, filePath):
    log = open(filePath, 'w+')
    for path in paths:
        log.write(str(path) + '\n')
    log.close()
    print(f'Finished writing {filePath} \n')


if __name__ == '__main__':
    main()
