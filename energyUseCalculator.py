import math

# From computation for kWh: 
# t * (nc*PowerDrawCPU*cpu + MemoryUse*PowerDrawMemory)*PUE*0.001
# t is in hours
# Here nc = 4, PUE = 1, PowerDrawCPU = 10.8W per core 
# PowerDrawMemory = 0.3725 W/GB
# MemoryUse = 6 GB

# Energy use in W is then 
# t*(cpu*43.2 + 2.235)

cpu = 0.9
durationMS = 4

energyUse = (durationMS/3600.0)*(cpu*43.2 + 2.235)

print(energyUse)
