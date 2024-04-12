import math

cpu = 0.07
durationMS = 6

energyUse = (durationMS/3600.0)*(cpu*43.2 + 2.235)

print(energyUse)
