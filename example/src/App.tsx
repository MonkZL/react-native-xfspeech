import * as React from 'react';

import { StyleSheet, View, Text } from 'react-native';
import { init } from 'react-native-xfspeech';

export default function App() {
  React.useEffect(() => {
    init('72da4785').then(() => {});
  }, []);

  return (
    <View style={styles.container}>
      <Text>Result: {111}</Text>
    </View>
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
