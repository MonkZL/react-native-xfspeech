import React, { useEffect } from 'react';

import { StyleSheet, Text, TouchableOpacity } from 'react-native';
import {
  init,
  onBeginOfSpeech,
  onEndOfSpeech,
  onError,
  onResult,
  start,
} from 'react-native-xfspeech';

export default function App() {
  useEffect(() => {
    init('72da4785').then(() => {
      console.log('initSdk');
    });
    let onBeginOfSpeechListener = onBeginOfSpeech(() => {
      console.log('onBeginOfSpeech');
    });
    let onEndOfSpeechListener = onEndOfSpeech(() => {
      console.log('onEndOfSpeech');
    });
    let onResultListener = onResult((event) => {
      console.log('onResult', event);
    });
    let onErrorListener = onError((event) => {
      console.log('onError', event);
    });
    return () => {
      onBeginOfSpeechListener.remove();
      onEndOfSpeechListener.remove();
      onResultListener.remove();
      onErrorListener.remove();
    };
  }, []);
  return (
    <TouchableOpacity
      onPress={() => {
        start({}).then(() => {
          console.log('start');
        });
      }}
      style={styles.container}
    >
      <Text>Result: {111}</Text>
    </TouchableOpacity>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    alignItems: 'center',
    justifyContent: 'center',
  },
  box: {
    width: 60,
    height: 60,
    marginVertical: 20,
  },
});
