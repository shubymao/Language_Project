import tkinter as tk
from tsm import genPts, greedySearch
from time import sleep

DELAY = 0.001
WIDTH = 600
HEIGHT = 600
BORDER = 20
RADIUS = 2
n = 100

def main():
    root = tk.Tk()
    tk.Canvas.create_circle = _create_circle
    canvas = tk.Canvas(root, width=WIDTH, height=HEIGHT)
    canvas.pack()
    cords = genPts(n)
    drawPts(canvas,cords)
    updateFunc = getUpdateFunction(canvas)
    root.after(0,lambda : greedySearch(cords, update=updateFunc))
    root.mainloop()
    
    
    


def drawPts(canvas, cords):
    pointsIDs = []
    for (x,y) in list(map(mapCord, cords)):
        pointID = canvas.create_circle(x,y,3,fill='black')
        pointsIDs.append

def getUpdateFunction(canvas):
    lineId = None
    def refresh(pts):
        nonlocal canvas, lineId
        if lineId : canvas.delete(lineId)
        pts.append(pts[0])
        pts = list(map(mapCord, pts))
        lineId = canvas.create_line(pts, fill='red')
        canvas.update()
        sleep(DELAY)
        
    return refresh    

def _create_circle(self, x, y, r, **kwargs):
    return self.create_oval(x-r, y-r, x+r, y+r, **kwargs)

def mapCord(cord):
    x = BORDER + (WIDTH-2*BORDER)*cord[0]
    y = BORDER + (HEIGHT-2*BORDER)*cord[1]
    return (x,y)

if __name__ == '__main__':
    main()