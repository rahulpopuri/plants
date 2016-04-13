import os,time
from subprocess import call

TODAY = time.strftime('%Y%m%d')
DIRECTORY = "/mnt/nas_plants/" + TODAY


max_seq = 0
for file in os.listdir(DIRECTORY):
	seq_num = int(file.split("_")[1].split(".")[0]);
	if(max_seq < seq_num):
		max_seq = seq_num;

print "Max sequence found: " + str(max_seq);
new_seq = max_seq + 1;
print "Next sequence number: " + "%03d" % (new_seq)

call("fswebcam -r 1280x720 --no-banner " + DIRECTORY + "/" + TODAY + "_" + "%03d" % (new_seq) + ".jpg", shell=True) 
