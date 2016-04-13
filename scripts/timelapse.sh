#!/bin/bash

DATE=$(date +"%Y%m%d")
echo Using $DATE as date

ffmpeg -f image2 -r 10 -i "/mnt/nas_plants/$DATE/${DATE}_%03d.jpg" -r 10 -s hd720 -vcodec libx264 /mnt/nas_plants/videos/$DATE.mp4
