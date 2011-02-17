#!/bin/sh

# this is a SAMPLE of how to write a test using sh
# please make a copy of this and delete everything below the
# function definitions to write your own tests

# usage:
#  ./test.sh [-v]  (without -v: silent except for failures)
#  tail -f log.txt & ./test.sh  (tracks log file)

. ./settings.sh

if [ "$1" == "-v" ] ; then
    verbose=1
fi

date > log.txt

function test() {
    TEST=$1
    msg="=== $TEST"
    if [ $verbose ] ; then
   echo $msg
    fi
    echo $msg >> log.txt
}

function fail() {
    msg="FAILURE: $TEST failed $*  $!"
    echo $msg
    echo $msg >> log.txt
}

function compare() {
    diff "$1" "$2" 2>>log.txt
}

#####

test "Remote copy master image"

# note: must copy/fetch two images with same name, to avoid getting
# fooled by leftovers

SRC=media/jguru.gif

scp -q $SRC $SERVER:$DIR_MASTER/test.gif || fail
./geturl.sh $URL_MASTER/test.gif > $TMP/test.gif || fail
compare $SRC $TMP/test.gif || fail

SRC=media/NeoPS-logo.gif

scp -q $SRC $SERVER:$DIR_MASTER/test.gif || fail
./geturl.sh $URL_MASTER/test.gif > $TMP/test.gif || fail
compare $SRC $TMP/test.gif || fail

###

SRC=media/jguru.gif

test "Put master image (raw)"
./putmaster.sh $SRC test gif >>log.txt || fail

test "Get master image (raw)"
./getmaster.sh test gif | compare - $SRC >>log.txt || fail

###

test "Get thumbnail image"
SRC=media/hummus.jpg
./putmaster.sh $SRC hummus jpeg || fail

# test default format = jpeg
test "Get thumbnail: 100"
./getthumb.sh hummus 100 > $TMP/thumb.jpeg || fail
compare $TMP/thumb.jpeg media/hummus-100.jpeg || fail

test "Get thumbnail: 100 jpeg"
./getthumb.sh hummus 100 jpeg > $TMP/thumb.jpeg || fail
compare $TMP/thumb.jpeg media/hummus-100.jpeg || fail

test "Get thumbnail: 100 gif"
./getthumb.sh hummus 100 gif > $TMP/thumb.gif || fail
compare $TMP/thumb.gif media/hummus-100.gif || fail

test "Get thumbnail: 72 gif"
./getthumb.sh hummus 72 gif > $TMP/thumb.gif || fail "getting"
compare $TMP/thumb.gif media/hummus-72.gif || fail "comparing"

###

test "Get master image"
./geturl.sh "${CGI_IMAGE}?master=hummus&format=jpeg" > $TMP/test.jpeg || fail
compare media/hummus.jpg $TMP/test.jpeg || fail

###

test "Get scaled image: width"
./geturl.sh "${CGI_IMAGE}?master=hummus&width=100" > $TMP/test.jpeg || fail
compare media/hummus-100x66.jpeg $TMP/test.jpeg || fail

###

test "Get scaled image: height"
./geturl.sh "${CGI_IMAGE}?master=hummus&height=75" > $TMP/test.jpeg || fail
compare media/hummus-114x75.jpeg $TMP/test.jpeg || fail

###

test "Convert image (gif->jpeg)"
scp -q media/jguru.gif $SERVER:$DIR_MASTER/jguru.gif || fail copying
./geturl.sh "${CGI_IMAGE}?master=jguru&format=jpeg" > $TMP/test.jpeg || fail fetching
cp $TMP/test.jpeg $TMP/converted.jpeg
compare $TMP/test.jpeg media/jguru.jpeg || fail comparing

###

test "Get master image info"
./headurl.sh "${CGI_IMAGE}?master=hummus" > $TMP/head.txt || fail
cat $TMP/head.txt >>log.txt
grep "^X-Image-Height: 592$" $TMP/head.txt > /dev/null || fail X-Image-Height
grep "^X-Image-Width: 896$" $TMP/head.txt  > /dev/null || fail X-Image-Width
grep "^Content-Length: 77793$" $TMP/head.txt > /dev/null || fail Content-Length
grep "^Content-Type: image/jpeg$" $TMP/head.txt > /dev/null || fail Content-Type

test "Get scaled image info"
./headurl.sh "${CGI_IMAGE}?master=hummus&width=100" > $TMP/head.txt || fail
cat $TMP/head.txt >>log.txt
grep "^X-Image-Height: 66$" $TMP/head.txt > /dev/null || fail X-Image-Height
grep "^X-Image-Width: 100$" $TMP/head.txt  > /dev/null || fail  X-Image-Width
grep "^Content-Length: 2241$" $TMP/head.txt > /dev/null || fail Content-Length
grep "^Content-Type: image/jpeg$" $TMP/head.txt > /dev/null || fail Content-Type

test "Get scaled/converted image info"
./headurl.sh "${CGI_IMAGE}?master=hummus&width=100&format=gif" > $TMP/head.txt || fail "getting"
cat $TMP/head.txt >>log.txt
grep "^X-Image-Height: 66$" $TMP/head.txt > /dev/null || fail X-Image-Height
grep "^X-Image-Width: 100$" $TMP/head.txt  > /dev/null || fail X-Image-Width
grep "^Content-Length: 8288$" $TMP/head.txt > /dev/null || fail Content-Length
grep "^Content-Type: image/gif$" $TMP/head.txt > /dev/null || fail Content-Type

###

test "Flush thumbnail cache"
# get a thumbnail once
./getthumb.sh hummus 100 jpeg > /dev/null ||fail "get once"
# make sure it's in the cache
./geturl.sh "${URL_THUMB}/hummus-100.jpeg" > /dev/null || fail "get cache"
# flush the cache
./geturl.sh "${CGI_FLUSH}" >>log.txt || fail "flush"
# make sure it's *not* in the cache
./geturl.sh "${URL_THUMB}/hummus-100.jpeg" >/dev/null && fail "not in cache"

###

test "Flush scaled image cache"
# get a scaled image once
./geturl.sh "${CGI_IMAGE}?master=hummus&width=100" >/dev/null || fail
# make sure it's in the cache
./geturl.sh "${URL_SCALED}/hummus-100x66.jpeg" >/dev/null || fail "getting cached"
# flush the cache
# note: it's the same command to flush both thumb and scaled caches
./geturl.sh "${CGI_FLUSH}" >>log.txt || fail "flushing"
# make sure it's *not* in the cache
./geturl.sh "${URL_SCALED}/hummus-100x66.jpeg" >/dev/null && fail "not in cache"

###

test "Upload master"

java Telnet $SERVER $TMP/upload < media/upload-2.bin 2>>log.txt || fail uploading
id=`./getid.sh $TMP/upload`
./geturl.sh "${CGI_IMAGE}?master=${id}&format=gif" >$TMP/master.gif || fail downloading
compare $TMP/master.gif media/2.gif || fail comparing

###

test "Rotate master image"
./geturl.sh "${CGI_TRANSFORM}?master=jguru&format=gif&rotate=90" > $TMP/test.gif || fail "getting image (90)"
compare $TMP/test.gif "media/jguru+90.gif" || fail

./headurl.sh "${CGI_TRANSFORM}?master=jguru&format=gif&rotate=90" > $TMP/head || fail getting head
id=`./getid.sh $TMP/head`
./geturl.sh "${CGI_IMAGE}?master=${id}&format=gif" >$TMP/test.gif || fail getting by id
compare $TMP/test.gif "media/jguru+90.gif" || fail

./geturl.sh "${CGI_TRANSFORM}?master=jguru&format=gif&rotate=180" > $TMP/test.gif || fail "getting image (180)"
compare $TMP/test.gif "media/jguru+180.gif" || fail

./geturl.sh "${CGI_TRANSFORM}?master=jguru&format=gif&rotate=270" > $TMP/test.gif || fail "getting image (270)"
compare $TMP/test.gif "media/jguru+270.gif" || fail
