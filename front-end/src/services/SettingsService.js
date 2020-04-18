export default class SettingsService {
  constructor (settings) {
    this.settings = settings
  }

  get backendHost () {
    return this.settings.backendHost
  }

  get campgroundId () {
    return this.settings.campgroundId
  }

  static injectionDependencies () {
    return ['settings']
  }
}
