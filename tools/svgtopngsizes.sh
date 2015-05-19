#!/bin/bash -x

# Usage: ./svgtopngsizes.sh <source-svg> <destination-dir>
for x in 16 32 128 256 512; do
	rsvg-convert -h $x $1 > $2/icon_${x}x${x}.png
	rsvg-convert -h $((2*x)) $1 > $2/icon_${x}x${x}\@2x.png
done
