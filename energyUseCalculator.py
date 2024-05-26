import math

# From computation for kWh: 
# t * (nc*PowerDrawCPU*cpu + MemoryUse*PowerDrawMemory)*PUE*0.001
# t is in hours
# Here nc = 4, PUE = 1, PowerDrawCPU = 10.8W per core 
# PowerDrawMemory = 0.3725 W/GB
# MemoryUse = 6 GB

# Energy use in W is then 
# t*(cpu*43.2 + mem*0.3725)

cpustring = input("Input CPU utilisation 0 to 1: ")
cpu = float(cpustring)

durstring = input("Input duration in milliseconds: ")
durationMS = int(durstring)

memstring = input("Input memory use in GB: ")
memuse = float(memstring)

energyUse = (durationMS/3600.0)*(cpu*43.2 + memuse*0.3725)

print(energyUse)
