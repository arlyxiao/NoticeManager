class HomeController < ApplicationController

  def index

    title = '测试 title'
    desc = '测试 desc'
    other = '测试 other'

    render :json => {
      :title => title,
      :desc => desc,
      :other => other
    }
  end

end
