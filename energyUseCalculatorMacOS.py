import math

# From computation for kWh: 
# t * (nc*PowerDrawCPU*cpu + MemoryUse*PowerDrawMemory)*PUE*0.001
# t is in hours
# Here nc = 8, PUE = 1, PowerDrawCPU = 13.52375W per core 
# PowerDrawMemory = 0.3725 W/GB
# MemoryUse = mem

# Energy use in W is then 
# t*(cpu*108.19 + mem*0.3725)

cpustring = input("Input CPU utilisation 0 to 1: ")
cpu = float(cpustring)

durstring = input("Input duration in milliseconds: ")
durationMS = float(durstring)

memstring = input("Input memory use in GB: ")
memuse = float(memstring)

energyUse = (durationMS/3600.0)*(cpu*108.19 + memuse*0.3725)

print(energyUse)
